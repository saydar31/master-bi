// init.js
db = db.getSiblingDB('smart-city'); // switch to your target DB

// Create a collection and insert documents
db.users.insertMany([
 {
   "dow": "SUN",
   "count": 10
 },
 {
   "dow": "MON",
   "count": 20
 },
 {
   "dow": "TUE",
   "count": 15
 },
 {
   "dow": "WED",
   "count": 14
 }
);
