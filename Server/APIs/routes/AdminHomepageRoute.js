"use strict";
const AdminHomepageController = require("../controllers/AdminHomepageController");

module.exports = function(app) {
  app.route("/").get(AdminHomepageController.go_to_admin_page);
};
