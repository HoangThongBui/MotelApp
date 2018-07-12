"use strict";

const mongoose = require("mongoose");
const Post = mongoose.model("Post");
const User = mongoose.model("User");
const Room = mongoose.model("Room");

exports.get_posts_by_city = async function (req, res) {
    try {
        var city = req.body.city;
        var roomsInCity = await Room.find({city}, {_id : 1});
        var posts = await Post.find({status: "c", "room" : {"$in" : roomsInCity}}, {status : 0}).sort({request_date : -1});
        for (var i = 0; i < posts.length; i++){
            posts[i].room = await Room.findById(posts[i].room);
            posts[i].user = await User.findById(posts[i].user, {password : 0, status : 0, role : 0});
        }
        // await waitTimeOut();
        res.json(posts);
    } catch (error) {
        console.log(error);
        res.send("Server internal error!");
    }

};

exports.get_posts_by_user = async function (req,res) {
    await waitTimeOut();
    try {
        var user_id = req.params.user_id;
        var posts = await Post.find({user: user_id, status : {"$ne" : "r"}}).sort({request_date : -1});
        for (var i = 0; i < posts.length; i++){
            posts[i].room = await Room.findById(posts[i].room);
        }
        res.json(posts);
    } catch (error) {
        console.log(error);
        res.send("Server internal error!");
    }
}

exports.make_a_new_post = async function (req,res) {
    await waitTimeOut();
    try {
        var newRoom = new Room({
            address: req.body.address,
            city: req.body.city,
            district: req.body.district,
            ward: req.body.ward,
            price: req.body.price,
            area: req.body.area,
            description: req.body.description
        })
        var newRoomId = (await newRoom.save())._id;
        var newPost = new Post({
            title : req.body.title,
            user: req.body.user_id,
            room: newRoomId,
            request_date: new Date()
        });
        await newPost.save();
        res.send("Added new post!");
    } catch (error) {
        console.log(error);
        res.send("Server internal error!");
    }
}

//server waiting simulation
function waitTimeOut(){
    return new Promise((res,rej)=> {setTimeout(res, 1000)});
}
