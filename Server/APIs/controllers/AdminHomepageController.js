"use strict";

const mongoose = require('mongoose');
const Post = mongoose.model('Post');

exports.go_to_admin_page = async function(req, res) {
  res.render('login_page');
};

exports.test_server = function(req,res){
  res.send("Server is online!")
}
