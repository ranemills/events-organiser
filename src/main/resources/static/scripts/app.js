angular.module("app", [])
.constant('_', window._)
.controller("home", function (_, $http, $location) {
  var self = this;
  var userPromise = $http.get("/api/user").success(function (data) {
    self.user = _.get(data, 'userAuthentication.details.name', null);
    self.authenticated = self.user !== null;
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
          count: responseCounts['yes'],
          people: _.filter(event.invitations, {response: 'yes'})
        },
        no: {
          count: responseCounts['no'],
          people: _.filter(event.invitations, {response: 'no'})
        },
        maybe: {
          count: responseCounts['maybe'],
          people: _.filter(event.invitations, {response: 'maybe'})
        },
        'No Response': {
          count: responseCounts['no_response'],
          people: _.filter(event.invitations, {response: 'no_response'})
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
    var personNames = _.transform(event.responses, function(result, value) {
      result.push(value.people);
    }, []);
    return _.filter(self.people, function (person) {
      return !_.includes(personNames, person.name);
    });
  };

  self.invitePerson = function (eventId) {
    $http.put('/api/events/' + eventId + '/' + self.invite[eventId].id, {response: 'no_response'}).then(function (response) {
      var event = _.find(self.events, {id: eventId});
      _.remove(self.events, {id: eventId});
      event.response = response.data.response;
      self.events.push(event);
    });
  };

  self.addEvent = function () {
    if(!_.isEmpty(self.newEventName)) {
      $http.post('/api/events', {name: self.newEventName}).then(function(response) {
        self.events.push(createEvent(response.data));
      });
    }
  };

  self.updateResponse = function(event, person, newResponse) {
    $http.put('/api/events/'+event.id+'/'+person.id, {response: newResponse}).then(function(response) {
      var event = _.find(self.events, {id: event.id});
      _.remove(self.events, {id: event.id});
      event.response = response.data.response;
      self.events.push(event);
    });
  };
  
  self.people = [];
  self.events = [];
  self.newEventName = '';
});