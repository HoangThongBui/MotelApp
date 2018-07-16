"use strict";
const LoginController = require("../controllers/LoginController");

module.exports = function(app) {

  app.route("/")
    .get(LoginController.test_server);

  app.route("/admin/login")
      .get(LoginController.new_loading)
      .post(LoginController.login);
};
