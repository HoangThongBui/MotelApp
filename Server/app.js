var createError = require("http-errors");
var express = require("express");
var path = require("path");
var cookieParser = require("cookie-parser");
var logger = require("morgan");
const mongoose = require("mongoose");
var session = require('express-session');

var app = express();

//Model implementation
const
  Room = require("./models/RoomModel"),
  User = require("./models/UserModel"),
  Post = require("./models/PostModel"),
  PostReport = require("./models/PostReportModel"),
  Comment = require("./models/CommentModel");

// view engine setup (for web admin)
app.use(express.static(path.join(__dirname, "public")));
app.set("views", [path.join(__dirname, "webadmin")]);
app.set("view engine", "ejs");

//Database connection
try {
  mongoose.connect("mongodb://localhost:27017/hoteldb");
} catch (error) {
  console.log(error);
}

//middlewares implementation
app.use(logger("dev"));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(session({secret : "Admin is coming!"}));



//Route Implementation

//user
const PostRoute = require("./APIs/routes/PostRoute");
PostRoute(app);
const CommentRoute = require('./APIs/routes/CommentRoute');
CommentRoute(app);
const UserRoute = require("./APIs/routes/UserRoute");
UserRoute(app);

//admin
const LoginRoute = require('./WebAdminMVC/routes/LoginRoute');
LoginRoute(app);


//checkSession to protect route
app.use(require('./custom_modules/session'));


const HomepageRoute = require("./WebAdminMVC/routes/HomepageRoute");
HomepageRoute(app);
const UserpageRoute = require("./WebAdminMVC/routes/UserpageRoute");
UserpageRoute(app);
const PostpageRoute = require("./WebAdminMVC/routes/PostpageRoute");
PostpageRoute(app);
const ReportpageRoute = require("./WebAdminMVC/routes/ReportpageRoute");
ReportpageRoute(app);
const PostDetailRoute = require('./WebAdminMVC/routes/PostDetailRoute');
PostDetailRoute(app);


// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get("env") === "development" ? err : {};

  // render the error page
  res.status(err.status || 500);
  console.log(err);
  res.render("error");
});

module.exports = app;
