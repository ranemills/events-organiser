angular.module("app", ['angularMoment'])
  .constant('_', window._)
.service('AuthenticationService', function($http) {
  let authenticated = false;
  let user = null;

  let authenticate = function() {
    return $http.get("/api/user").then(function (response) {
      user = _.get(response.data, 'userAuthentication.details.name', null);
      authenticated = self.user !== null;
    }, function () {
      user = "N/A";
      authenticated = false;
    });
  };

  let logout = function () {
    $http.post('/logout', {}).success(function () {
      authenticated = false;
      $location.path("/");
    }).error(function (data) {
      console.log("Logout failed");
      authenticated = false;
    });
  };

  let getUser = () => user;
  let isAuthenticated = () => authenticated;

  return {
    authenticate,
    logout,
    getUser,
    isAuthenticated
  }

})
.run((AuthenticationService) =>
{
  AuthenticationService.authenticate();
})
  .controller("home", function ($scope, AuthenticationService) {
    let self = this;
    self.authenticated = false;

    $scope.$watch(AuthenticationService.isAuthenticated, () => self.authenticated = AuthenticationService.isAuthenticated());
  })
.component('navBar', {
  templateUrl: 'html/components/nav-bar.html',
  controller: function($scope, AuthenticationService) {
    let self = this;
    self.user = null;
    self.authenticated = false;
    self.logout = AuthenticationService.logout;

    $scope.$watch(AuthenticationService.isAuthenticated, function() {
      self.user = AuthenticationService.getUser();
      self.authenticated = AuthenticationService.isAuthenticated();
    });
  }
})
.component('mainView', {
  templateUrl: 'html/components/main-view.html',
  controller: function(_, $http, $location, AuthenticationService) {
    let self = this;

    $http.get('/api/events').then(function (response) {
      self.events = response.data;
      _.each(self.events, setResponseCounts);
    });
    $http.get('/api/people').then(function (response) {
      self.people = response.data;
    });

    self.peopleNotInEvent = function (event) {
      let personNames = _.map(event.invitations, 'name');
      return _.filter(self.people, (person) => !_.includes(personNames, person.name));
    };

    self.invitePerson = function (event) {
      let personId = self.invite[event.id].id;
      $http.put('/api/events/' + event.id + '/' + personId, {response: 'no_response'}).then(function (response) {
        let invitation = response.data;
        invitation.name = self.invite[event.id].name;
        invitation.id = personId;

        event.invitations.push(invitation);
      });
    };

    self.addEvent = function () {
      if (!_.isEmpty(self.newEventName)) {
        $http.post('/api/events', {name: self.newEventName}).then(function (response) {
          let event = response.data;
          setResponseCounts(event);
          self.events.push(event);
        });
      }
    };

    self.updateResponse = function (event, person, newResponse) {
      $http.put('/api/events/' + event.id + '/' + person.id, {response: newResponse}).then(function (response) {
        _.merge(person, response.data);
        setResponseCounts(event);
      });
    };

    function setResponseCounts(event) {
      let counts = _.countBy(event.invitations, 'response');
      event.responseCounts = {
        yes: _.get(counts, 'yes', 0),
        maybe: _.get(counts, 'maybe', 0),
        no: _.get(counts, 'no', 0),
        no_response: _.get(counts, 'no_response', 0)
      }
    }

    self.people = [];
    self.events = [];
    self.newEventName = '';

    self.view = 'list';

    self.newEvent = function() {
      self.view = 'add';
    }
  }
})
  .component('eventCard', {
    templateUrl: 'html/components/event-card.html',
    bindings: {
      event: '='
    }
  })
  .component('addEvent', {
    binding: {
      'finished': '&finishedFn'
    },
    templateUrl: 'html/components/add-event.html',
    controller: function($http, $q) {
      let self = this;

      // Lifecycle bindings
      self.$onInit = function() {
        $http.get('/api/people').then(function (response) {
          self.people = response.data;
        });
        self.selectedPeople = {};
      };

      // Custom functions
      self.save = function() {
        $http.post('/api/events', {name: self.newEventName}).then(function (response) {
          let eventId = response.data.id;
          let promises = [];
          _.each(self.selectedPeople, (value, id) => {
            if (value) {
              promises.push($http.put('/api/events/' + eventId + '/' + id, {response: 'no_response'}));
            }
          });
          $q.all(promises).then(() => self.add(event));
        });
      };
    }
  });