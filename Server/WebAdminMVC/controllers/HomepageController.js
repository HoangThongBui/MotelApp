'use strict'

exports.go_to_home_page = (req,res) => {
    res.render('home', {page_type : 'home'});
}

exports.home_redirect = (req,res) => {
    res.redirect('/admin/home');
}