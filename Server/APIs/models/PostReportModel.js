const mongoose = require("mongoose");

const PostReportSchema = new mongoose.Schema({
  post_id: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "Post"
  },
  report_time: {
    type: Date,
    default: new Date.now()
  },
  report_status: Boolean
});

module.exports = mongoose.model("PostReport", PostReportSchema);
