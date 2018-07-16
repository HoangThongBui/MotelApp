"use strict";

const mongoose = require('mongoose');
const User = mongoose.model('User');

exports.test_server = function (req, res) {
  res.send("Server is online!");
}

exports.new_loading = (req, res) => {
  res.render('login_page');
}

exports.login = async (req, res) => {
  try {
    var email = req.body.email;
    var password = req.body.password;
    var user = await User.findOne({ email });
    if (user === null) {
      res.render('login_page', { error: 'Admin not found!' });
    }
    else {
      if (password === user.password) {
        req.session.admin = user.name;
        res.redirect('/admin/home');
      }
      else {
        res.render('login_page', { error: 'Wrong password!' });
      }
    }
  } catch (error) {
    res.send(error);
  }
}

exports.logout = async (req, res) => {
    await req.session.destroy();
    res.locals.admin = undefined;
    res.redirect('/admin/login');
}
