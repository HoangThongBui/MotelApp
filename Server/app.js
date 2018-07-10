var createError = require("http-errors");
var express = require("express");
var path = require("path");
var cookieParser = require("cookie-parser");
var logger = require("morgan");
const mongoose = require("mongoose");

var app = express();

//Model implementation
const 
  User = require("./APIs/models/UserModel"),
  Post = require("./APIs/models/PostModel"),
  PostReport = require("./APIs/models/PostReportModel"),
  Comment = require("./APIs/models/CommentModel");

// view engine setup (for web admin)
app.set("views", [path.join(__dirname, "webadmin"),
                  path.join(__dirname,"webadmin/head")]);
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
app.use(express.static(path.join(__dirname, "public")));

//Route Implementation
const AdminPageRoute = require("./APIs/routes/AdminHomepageRoute");
AdminPageRoute(app);
const PostRoute = require("./APIs/routes/PostRoute");
PostRoute(app);
const CommentRoute = require('./APIs/routes/CommentRoute');
CommentRoute(app);

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
  res.render("error");
});

module.exports = app;
