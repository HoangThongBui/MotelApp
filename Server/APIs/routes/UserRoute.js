'use strict'

const UserController = require('../controllers/UserController');
const Multer = require('../../custom_modules/multer');

module.exports = function(app){
    app.route('/user/api/check_user_status/:user_id')
        .get(UserController.check_user_status);

    app.route('/user/api/login')
        .post(UserController.user_login);

    app.route('/user/api/register')
        .post(UserController.user_register);

    app.route('/user/api/get_user_by_id/:user_id')
        .get(UserController.get_user_by_id);

    app.route('/user/api/update_profile/:user_id')
        .put(Multer.single('avatar'), UserController.update_profile);
}