"use strict";

const mongoose = require('mongoose');
const User = mongoose.model('User');

exports.test_server = function(req,res){
  res.send("Server is online!")
}

exports.go_to_admin_page = async function(req, res) {
  res.render('home');
};

exports.new_loading = async (req,res) => {  
  res.render('login_page');
}

exports.login = async (req,res) => {
  try {
    var email = req.body.email;
    var password = req.body.password;
    var user = await User.findOne({email});
    if (user === null){
      res.render('login_page', {error : 'Admin not found!'});
    }
    else {
      if (password === user.password){
        res.redirect('/admin/home');
      }
      else {
        res.render('login_page', {error : 'Wrong password!'});
      }
    }
  } catch (error) {
    res.send(error);
  }
}
