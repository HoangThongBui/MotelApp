"use strict";

const mongoose = require("mongoose");
const Post = mongoose.model("Post");
const User = mongoose.model("User");
const Room = mongoose.model("Room");

exports.get_posts_by_city = async function (req, res) {
    try {
        var city = req.body.city;
        city = "TPHCM";
        var roomsInCity = await Room.find({city}, {_id : 1});
        console.log(roomsInCity);
        var posts = await Post.find({status: true, "room" : {"$in" : roomsInCity}}, {status : 0}).sort({request_date : -1});
        console.log(posts);
        for (var i = 0; i < posts.length; i++){
            posts[i].room = await Room.findById(posts[i].room);
            posts[i].user = await User.findById(posts[i].user, {password : 0, status : 0, role : 0});
        }
        // await waitTimeOut();
        res.json(posts);
    } catch (error) {
        res.send(error)
    }

};

//server waiting simulation
function waitTimeOut(){
    return new Promise((res,rej)=> {setTimeout(res, 3000)});
}
