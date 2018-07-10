'use strict'

const UserController = require('../controllers/UserController');

module.exports = function(app){
    app.route('/user/api/check_user_status/:id')
        .get(UserController.check_user_status);

    app.route('/user/api/login')
        .post(UserController.user_login);
}