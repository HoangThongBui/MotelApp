"use strict";
const LoginController = require("../controllers/LoginController");
const SessionChecker = require('../../custom_modules/session');

module.exports = function (app) {

  app.route("/")
    .get(LoginController.test_server);

  app.route("/admin/login")
    .get(LoginController.new_loading)
    .post(LoginController.login);

  app.route("/admin/logout")
    .get(LoginController.logout);
};
