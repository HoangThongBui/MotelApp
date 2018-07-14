'use strict'

const mongoose = require('mongoose');
const User = mongoose.model('User');

exports.check_user_status = async function (req, res) {
    try {
        var id = req.params.user_id;  
        var user = await User.findById(id, { password: 0 });
        if (user.status) {
            res.send("User is active!");
        }
        else {
            res.send("User is banned!");
        }
    } catch (error) {
        console.log(error);
        res.send("Server internal error!");
    }
}

exports.user_login = async function (req, res) {
    await waitTimeOut();
    try {
        var email = req.body.email;
        var password = req.body.password;
        var user = await User.findOne({ email });
        if (user){
            if (!user.status){
                res.send("User is banned!");
            }
            if (password === user.password){
                res.send(user._id.toString());
            }
            else {
                res.send("Wrong password!")
            }
        }
        else {
            res.send("No account!")
        }
    } catch (error) {
        console.log(error);
        res.send("Server internal error!");
    }
}

exports.user_register = async function(req,res){
    try {
        await waitTimeOut();
        var existed_user = await User.findOne({email: req.body.email});
        if (existed_user){
            res.send("User existed!");
        }
        else {
            var newUser = new User({
                email : req.body.email,
                name: req.body.name,
                password: req.body.password,
                phone: req.body.phone,
            });
            await newUser.save();
            res.send("Register successfully!")
        }
    } catch (error) {
        console.log(error);
        res.send("Server internal error!");
    }
}

exports.get_user_by_id = async function (req,res){
    try {
        var id = req.params.user_id;
        var user = await User.findById(id, {status: 0, password: 0, role: 0});
        res.json(user);
    } catch (error) {
        console.log(error);
        res.send("Server internal error!");
    }
}

exports.change_avatar = async function (req,res) {
    try {
        var id = req.params.user_id;
        var image = '/images/users/' + req.file.filename;
        var set = {
            image
        };
        await User.findByIdAndUpdate(id, {"$set" : set});
        res.send('Avatar changed!')    
    } catch (error) {
        console.log(error);
        res.send("Server internal error!");
    }
}

//server waiting simulation
function waitTimeOut(){
    return new Promise((res,rej)=> {setTimeout(res, 1000)});
}