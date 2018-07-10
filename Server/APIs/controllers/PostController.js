"use strict";

const mongoose = require("mongoose");
const Post = mongoose.model("Post");
const User = mongoose.model("User");

exports.get_posts_by_city = async function (req, res) {
    try {
        var city = req.body.city;
        var postsOfCity = await Post.find({ city, status: true }).sort({ request_date: -1 });
        for (var i = 0; i < postsOfCity.length; i++) {
            postsOfCity[i].user = await User.findById(postsOfCity[i].user, {password : 0});
        }
        // await waitTimeOut();
        res.json(postsOfCity);
    } catch (error) {
        res.send(error)
    }

};

//server waiting simulation
function waitTimeOut(){
    return new Promise((res,rej)=> {setTimeout(res, 3000)});
}
