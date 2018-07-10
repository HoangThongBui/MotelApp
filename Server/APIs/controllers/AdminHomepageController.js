"use strict";

exports.go_to_admin_page = function(req, res) {
  res.render("login_page");
};

exports.test_server = function(req,res){
  res.send("Server is online!")
}
