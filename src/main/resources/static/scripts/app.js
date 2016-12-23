angular.module("app", ['angularMoment'])
  .constant('_', window._)
  .controller("home", function (_, $http, $location) {
    let self = this;

    let userPromise = $http.get("/api/user").then(function (response) {
      self.user = _.get(response.data, 'userAuthentication.details.name', null);
      self.authenticated = self.user !== null;
    }, function () {
      self.user = "N/A";
      self.authenticated = false;
    });

    userPromise.then(function () {
      if (self.authenticated) {
        $http.get('/api/events').then(function (response) {
          self.events = response.data;
          _.each(self.events, setResponseCounts);
        });
        $http.get('/api/people').then(function (response) {
          self.people = response.data;
        });
      }
    });

    self.logout = function () {
      $http.post('/logout', {}).success(function () {
        self.authenticated = false;
        $location.path("/");
      }).error(function (data) {
        console.log("Logout failed");
        self.authenticated = false;
      });
    };

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