'use strict'

module.exports = (req,res,next) => {
    if (req.session.admin){
        res.locals.admin = req.session.admin;
        next();
    }
    else {
        res.locals.admin = undefined;
        res.redirect('/admin/login');
    }
}