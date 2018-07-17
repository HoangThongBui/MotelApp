"use strict";

const mongoose = require("mongoose");
const Post = mongoose.model("Post");
const User = mongoose.model("User");
const Room = mongoose.model("Room");
const PostReport = mongoose.model("PostReport");
const Geo = require('geo-nearby');
const Geocoder = require('node-geocoder')({
    provider: 'google',
    httpAdapter: 'https',
    apiKey: 'AIzaSyDxms3Z0Qn3TLFpw-X7T7Yg17rYPZ2SCDY',
    formatter: null
});

exports.get_newest_posts = async function (req, res) {
    try {
        var newestPosts = await Post.find({ status: "c" }).sort({ request_date: -1 });
        for (var i = 0; i < newestPosts.length; i++) {
            newestPosts[i].room = await Room.findById(newestPosts[i].room);
            newestPosts[i].user = await User.findById(newestPosts[i].user, { password: 0, status: 0, role: 0 });
        }
        res.json(newestPosts);
    } catch (error) {
        console.log(error);
        res.send("Server internal error!");
    }
}

exports.get_posts_nearby = async function (req, res) {
    try {
        var currentLat = req.body.lat;
        var currentLon = req.body.lon;
        var confirmedPosts = await Post.find({status : "c"});
        var roomsOfConfirmedPosts = [];
        for (var i = 0; i < confirmedPosts.length; i++){
            roomsOfConfirmedPosts.push(await Room.findById(confirmedPosts[i].room));
        }                               
        var roomData = [];
        for (var i = 0; i < roomsOfConfirmedPosts.length; i++) {
            var geocodeResult = await Geocoder.geocode(
                roomsOfConfirmedPosts[i].address + ', ' +
                'Phường ' + roomsOfConfirmedPosts[i].ward + ', ' +
                'Quận ' + roomsOfConfirmedPosts[i].district + ', ' +
                roomsOfConfirmedPosts[i].city);
            roomData.push({
                id: roomsOfConfirmedPosts[i]._id.toString(),
                geocode: {
                    lat: geocodeResult[0].latitude,
                    lon: geocodeResult[0].longitude
                }
            });
        }
        var geoDataArray = [];
        for (var i = 0; i < roomData.length; i++) {
            var set = [];
            set.push(roomData[i].geocode.lat);
            set.push(roomData[i].geocode.lon)
            set.push(roomData[i].id);
            geoDataArray.push(set);
        }
        var geoDataSet = Geo.createCompactSet(geoDataArray);
        var geoCalculator = new Geo(geoDataSet, { sorted: true });
        var geoNearby = geoCalculator.nearBy(currentLat, currentLon, 5000);
        var roomsNearby = [];
        for (var i = 0; i < geoNearby.length; i++) {
            roomsNearby.push(geoNearby[i].i);
        }
        var posts = await Post.find({"room": { "$in": roomsNearby } }, { status: 0 }).sort({ request_date: -1 });
        for (var i = 0; i < posts.length; i++) {
            posts[i].room = await Room.findById(posts[i].room);
            posts[i].user = await User.findById(posts[i].user, { password: 0, status: 0, role: 0 });
        }
        // await waitTimeOut();
        res.json(posts);
    } catch (error) {
        console.log(error);
        res.send("Server internal error!");
    }
}

exports.get_posts_by_user = async function (req, res) {
    try {
        var user_id = req.params.user_id;
        var posts = await Post.find({ user: user_id, status: { "$nin": ["r", "d"] } }).sort({ request_date: -1 });
        for (var i = 0; i < posts.length; i++) {
            posts[i].room = await Room.findById(posts[i].room);
        }
        res.json(posts);
    } catch (error) {
        console.log(error);
        res.send("Server internal error!");
    }
}

exports.get_post_by_id = async function (req, res) {
    try {
        var post = await Post.findById(req.params.post_id);
        if (post.status === 'd') {
            res.json({});
        }
        else {
            post.room = await Room.findById(post.room);
            post.user = await User.findById(post.user, { password: 0, status: 0, role: 0 });
            res.json(post);
        }
    } catch (error) {
        console.log(error);
        res.send("Server internal error!");
    }
}

exports.make_a_new_post = async function (req, res) {
    try {
        var address = req.body.address;
        var city = req.body.city;
        var district = req.body.district;
        var ward = req.body.ward;
        var price = req.body.price;
        var area = req.body.area;
        var description = req.body.description
        var title = req.body.title;
        var user = req.body.user_id;
        var images = [];
        for (var i = 0; i < req.files.length; i++) {
            images.push('/images/posts/' + req.files[i].filename);
        }
        var newRoom = new Room({
            address, city, district, ward, price, area, description, images
        })
        var newRoomId = (await newRoom.save())._id;
        var newPost = new Post({
            title,
            user,
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

exports.search_post = async function (req, res) {
    try {
        var follow_price = req.body.follow_price === 'true';
        var min_price = req.body.min_price;
        var max_price = req.body.max_price;
        var follow_area = req.body.follow_area === 'true';
        var min_area = req.body.min_area;
        var max_area = req.body.max_area;
        var filter = {};
        if (follow_price) {
            filter['price'] = {
                "$gte": min_price,
                "$lte": max_price
            };
            if (!filter.price.$gte) {
                delete filter.price.$gte;
            }
            if (!filter.price.$lte) {
                delete filter.price.$lte;
            }
        }
        if (follow_area) {
            filter['area'] = {
                "$gte": min_area,
                "$lte": max_area
            };
            if (!filter.area.$gte) {
                delete filter.area.$gte;
            }
            if (!filter.area.$lte) {
                delete filter.area.$lte;
            }
        }
        var filter_rooms = await Room.find(filter, { _id: 1 });
        var posts = await Post.find({ status: "c", "room": { "$in": filter_rooms } }, { status: 0 }).sort({ request_date: -1 });
        for (var i = 0; i < posts.length; i++) {
            posts[i].room = await Room.findById(posts[i].room);
            posts[i].user = await User.findById(posts[i].user, { password: 0, status: 0, role: 0 });
        }
        // await waitTimeOut();
        res.json(posts);
    } catch (error) {
        console.log(error);
        res.send("Server internal error!");
    }
}

exports.edit_post_by_id = async function (req, res) {
    try {
        var editStatus = req.body.status;
        var currentStatus = await Post.findById(req.params.post_id).status;
        if (editStatus !== currentStatus){
            res.send("Post already confirmed!");
        }
        var address = req.body.address;
        var city = req.body.city;
        var district = req.body.district;
        var ward = req.body.ward;
        var price = req.body.price;
        var area = req.body.area;
        var description = req.body.description
        var title = req.body.title;
        var updateRoomSet = {
            address: req.body.address,
            city: req.body.city,
            district: req.body.district,
            ward: req.body.ward,
            price: req.body.price,
            area: req.body.area,
            description: req.body.description
        };
        if (req.files) {
            var images = [];
            for (var i = 0; i < req.files.length; i++) {
                images.push('/images/posts/' + req.files[i].filename);
            }
            updateRoomSet["images"] = images;
        }
        await Room.findByIdAndUpdate(req.body.room_id, { "$set": updateRoomSet });
        var updatePostSet = {
            title
        };
        await Post.findByIdAndUpdate(req.params.post_id, { "$set": updatePostSet });
        res.send("Updated!");
    } catch (error) {
        console.log(error);
        res.send("Server internal error!");
    }

}

exports.delete_post_by_id = async function (req, res) {
    try {
        var deletePostSet = {
            status: "d"
        };
        await Post.findByIdAndUpdate(req.params.post_id, { "$set": deletePostSet });
        var report_time = new Date();
        var newReport = new PostReport({
            post : req.params.post_id,
            user : req.body.user_id,
            from : req.body.status,
            to: "d",
            description: "Người dùng xoá bài đăng!",
            report_time
        });
        await newReport.save();
        res.send("Deleted!");
    } catch (error) {
        console.log(error);
        res.send("Server internal error!");
    }
}

//server waiting simulation
function waitTimeOut() {
    return new Promise((res, rej) => { setTimeout(res, 1000) });
}
