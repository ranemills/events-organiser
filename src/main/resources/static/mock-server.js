"use strict";

let express = require('express');
let bodyParser = require("body-parser");
let fs = require('fs');
let app = express();
let _ = require('lodash');

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

app.use(express.static('.'));

app.use("/api/user", function(req, res) {
  res.json({testMode: true, userAuthentication: {details: {name: 'Test User'}}});
});

addRoute(app, "/login/google");
// addRoute(app, "/api/events");
addRoute(app, "/api/people");


app.use('/api/events:query?', function(req, res) {
  res.redirect("http://localhost:8080/api/events" + _.get(req, 'query.query', ''));
});

function addRoute(app, urlPath) {
  app.use(urlPath, function(req, res) {
    res.redirect("http://localhost:8080" + urlPath);
  })
}

app.listen(8000, function() {
  console.log('mock server listening on port 8000');
});


