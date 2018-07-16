'use strict'

exports.go_to_user_page = (req,res) => {
    res.render('user', {page_type : 'user'});
}