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

  userPromise.then(function () {
    if (self.authenticated) {
      $http.get('/api/events').then(function (response) {
        self.events = response.data;
      });
      $http.get('/api/people').then(function (response) {
        self.people = response.data;
      });
    }
  });

  self.peopleNotInEvent = function (event) {
    var personNames = _.map(event.invitations, 'name');
    return _.filter(self.people, function (person) {
      return !_.includes(personNames, person.name);
    });
  };

  self.invitePerson = function (eventId) {
    $http.post('/api/events/' + eventId + '/invite?id=' + self.invite[eventId].id).then(function (response) {
      _.remove(self.events, {'id': eventId});
      self.events.push(response.data);
    });
  };

  self.people = [];
  self.events = [];
});