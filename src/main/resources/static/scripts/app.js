angular.module("app", [])
.constant('_', window._)
.controller("home", function (_, $http, $location) {
  var self = this;
  var userPromise = $http.get("/api/user").success(function (data) {
    self.user = data.userAuthentication.details.name;
    self.authenticated = true;
  }).error(function () {
    self.user = "N/A";
    self.authenticated = false;
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

  function createEvent(event) {
    var responseCounts = _.countBy(event.invitations, 'response');
    return {
      id: event.id,
      name: event.name,
      responses: {
        yes: {
          count: responseCounts['YES'],
          people: _.filter(event.invitations, {response: 'YES'})
        },
        no: {
          count: responseCounts['NO'],
          people: _.filter(event.invitations, {response: 'NO'})
        },
        maybe: {
          count: responseCounts['MAYBE'],
          people: _.filter(event.invitations, {response: 'MAYBE'})
        },
        noResponse: {
          count: responseCounts['NO_RESPONSE'],
          people: _.filter(event.invitations, {response: 'NO_RESPONSE'})
        }
      }
    }
  }

  userPromise.then(function () {
    if (self.authenticated) {
      $http.get('/api/events').then(function (response) {
        self.events = _.map(response.data, createEvent);
      });
      $http.get('/api/people').then(function (response) {
        self.people = response.data;
      });
    }
  });

  self.peopleNotInEvent = function (event) {
    var personNames = _.transform(event.responses, function(result, value, key) {
      result.push(value.people);
    }, []);
    return _.filter(self.people, function (person) {
      return !_.includes(personNames, person.name);
    });
  };

  self.invitePerson = function (eventId) {
    $http.post('/api/events/' + eventId + '/invite?id=' + self.invite[eventId].id).then(function (response) {
      _.remove(self.events, {'id': eventId});
      self.events.push(createEvent(response.data));
    });
  };

  self.addEvent = function () {
    if(!_.isEmpty(self.newEventName)) {
      $http.post('/api/events', {name: self.newEventName}).then(function(response) {
        self.events.push(createEvent(response.data));
      });
    }
  };

  self.people = [];
  self.events = [];
  self.newEventName = '';
});