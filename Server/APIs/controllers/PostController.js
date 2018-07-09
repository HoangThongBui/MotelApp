"use strict";

const mongoose = require("mongoose");
const Post = mongoose.model("Post");
const User = mongoose.model("User");

exports.get_posts_by_city = async function (req, res) {
    try {
        var city = req.params.city;
        var postsOfCity = await Post.find({ city, status: true }).sort({ request_date: 1 });
        for (var i = 0; i < postsOfCity.length; i++) {
            postsOfCity[i].user_id = await User.findById(postsOfCity[i].user_id);
        }
        return res.json(postsOfCity);
    } catch (error) {
        res.send(error)
    }

};
