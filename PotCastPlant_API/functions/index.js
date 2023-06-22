
const functions = require("firebase-functions");
const serviceAccount = require("./permissions.json");
const admin = require("firebase-admin");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

const express = require("express");
const app = express();
const cors = require("cors");
app.use(cors({origin: true}));

const db = admin.firestore();

// ROUTES

//  FOR POTS DATABASE //


// Create - Post
app.post("/api/pots/create", (req, res) => {
  (async () => {
    try {
      const initialSunlight = Array(7).fill(0);
      const temperature = 0;
      const moisture = 0;
      const waterlevel = 0;
      const plantId = 0;
      const humidity = 0;
      const automaticWatering = true;

      await db.collection("pots").doc("/" + req.body.id + "/")
          .create({
            id: req.body.id,
            temperature: temperature,
            moisture: moisture,
            sunlight: initialSunlight,
            waterlevel: waterlevel,
            humidity: humidity,
            plantId: plantId,
            automaticWatering: automaticWatering,
          });

      return res.status(200).send();
    } catch (error) {
      console.log(error);
      return res.status(500).send(error);
    }
  })();
});

// Read - Get a specific pot based on ID
app.get("/api/pots/read/:id", (req, res) => {
  (async () => {
    try {
      const document = db.collection("pots").doc(req.params.id);
      const pot = await document.get();
      const response = pot.data();

      return res.status(200).send(response);
    } catch (error) {
      console.log(error);
      return res.status(500).send(error);
    }
  })();
});


// Read All Pots
app.get("/api/pots/read/", (req, res) => {
  (async () => {
    try {
      const query = db.collection("pots");
      const response = [];

      await query.get().then((QuerySnapshot) => {
        const docs = QuerySnapshot.docs; // The result of the query

        for (const doc of docs) {
          const selectedItem = {
            id: doc.id,
            temperature: doc.data().temperature,
            moisture: doc.data().moisture,
            sunlight: doc.data().sunlight,
            waterlevel: doc.data().waterlevel,
            plantId: doc.data().plantId,
            automaticWatering: doc.data().automaticWatering,
          };
          response.push(selectedItem);
        }
        return response; // each will return a value
      });

      return res.status(200).send(response);
    } catch (error) {
      console.log(error);
      return res.status(500).send(error);
    }
  })();
});

// Update - Put

// app.put("/api/pots/update/:id", (req, res) => {
//   (async () => {
//     try {
//       const document = db.collection("pots").doc(req.params.id);

//       await document.update({
//         temperature: req.body.temperature,
//         moisture: req.body.moisture,
//         sunlight: req.body.sunlight,
//         waterlevel: req.body.waterlevel,
//       });

//       return res.status(200).send();
//     } catch (error) {
//       console.log(error);
//       return res.status(500).send(error);
//     }
//   })();
// });


// Enqueue and Dequeue
app.put("/api/pots/update/:id", (req, res) => {
  (async () => {
    try {
      const document = db.collection("pots").doc(req.params.id);
      await document.update({
        temperature: req.body.temperature,
        moisture: req.body.moisture,
        waterlevel: req.body.waterlevel,
        humidity: req.body.humidity,
      });

      return res.status(200).send();
    } catch (error) {
      console.log(error);
      return res.status(500).send(error);
    }
  })();
});


// UPDATE ONLY THE SUNLIGHT VALUES
app.put("/api/pots/update/sunlight/:id", (req, res) => {
  (async () => {
    try {
      const document = db.collection("pots").doc(req.params.id);

      // Retrieve the existing sunlight array from the document
      const snapshot = await document.get();
      const data = snapshot.data();
      const existingSunlight = data.sunlight || [];


      while (existingSunlight.length < 7) {
        existingSunlight.push(0);
      }

      existingSunlight.shift();
      existingSunlight.push(req.body.sunlight);

      await document.update({
        sunlight: existingSunlight,
      });

      return res.status(200).send();
    } catch (error) {
      console.log(error);
      return res.status(500).send(error);
    }
  })();
});


// Delete - Delete

app.delete("/api/pots/delete/:id", (req, res) => {
  (async () => {
    try {
      const document = db.collection("pots").doc(req.params.id);

      await document.delete();
      return res.status(200).send();
    } catch (error) {
      console.log(error);
      return res.status(500).send(error);
    }
  })();
});


// FOR PLANT DATABASE //


// Create - Post
app.post("/api/plants/create", (req, res) => {
  (async () => {
    try {
      await db.collection("plants").doc("/" + req.body.id + "/")
          .create({
            id: req.body.id,
            name: req.body.name,
            description: req.body.description,
            humidityMax: req.body.humidityMax,
            humidityMin: req.body.humidityMin,
            sunlightMax: req.body.sunlightMax,
            sunlightMin: req.body.sunlightMin,
            moistureMax: req.body.moistureMax,
            moistureMin: req.body.moistureMin,
            temperatureMax: req.body.temperatureMax,
            temperatureMin: req.body.temperatureMin,
          });

      return res.status(200).send();
    } catch (error) {
      console.log(error);
      return res.status(500).send(error);
    }
  })();
});

// Read - Get a specific plant based on ID
app.get("/api/plants/read/:id", (req, res) => {
  (async () => {
    try {
      const document = db.collection("plants").doc(req.params.id);
      const plant = await document.get();
      const response = plant.data();

      return res.status(200).send(response);
    } catch (error) {
      console.log(error);
      return res.status(500).send(error);
    }
  })();
});


// Read All Plants
app.get("/api/plants/read/", (req, res) => {
  (async () => {
    try {
      const query = db.collection("plants");
      const response = [];

      await query.get().then((QuerySnapshot) => {
        const docs = QuerySnapshot.docs; // The result of the query

        for (const doc of docs) {
          const selectedItem = {
            id: doc.id,
            name: doc.data().name,
            description: doc.data().description,
            humidityMax: doc.data().humidityMax,
            humidityMin: doc.data().humidityMin,
            sunlightMax: doc.data().sunlightMax,
            sunlightMin: doc.data().sunlightMin,
            moistureMax: doc.data().moistureMax,
            moistureMin: doc.data().moistureMin,
            temperatureMax: doc.data().temperatureMax,
            temperatureMin: doc.data().temperatureMin,
          };
          response.push(selectedItem);
        }
        return response; // each will return a value
      });

      return res.status(200).send(response);
    } catch (error) {
      console.log(error);
      return res.status(500).send(error);
    }
  })();
});

// Update - Put

app.put("/api/plants/update/:id", (req, res) => {
  (async () => {
    try {
      const document = db.collection("plants").doc(req.params.id);

      await document.update({
        name: req.body.name,
        description: req.body.description,
        humidityMax: req.body.humidityMax,
        humidityMin: req.body.humidityMin,
        sunlightMax: req.body.sunlightMax,
        sunlightMin: req.body.sunlightMin,
        moistureMax: req.body.moistureMax,
        moistureMin: req.body.moistureMin,
        temperatureMax: req.body.temperatureMax,
        temperatureMin: req.body.temperatureMin,
      });

      return res.status(200).send();
    } catch (error) {
      console.log(error);
      return res.status(500).send(error);
    }
  })();
});

// Delete - Delete

app.delete("/api/plants/delete/:id", (req, res) => {
  (async () => {
    try {
      const document = db.collection("plants").doc(req.params.id);

      await document.delete();
      return res.status(200).send();
    } catch (error) {
      console.log(error);
      return res.status(500).send(error);
    }
  })();
});

// Export the API to the Firebase Cloud Functions
exports.app = functions.https.onRequest(app);
