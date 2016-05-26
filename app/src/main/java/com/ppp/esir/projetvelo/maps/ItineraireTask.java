package com.ppp.esir.projetvelo.maps;

/**
 * Created by Salifou on 23/05/2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ppp.esir.projetvelo.utils.Datacontainer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * ItineraireTask
 *
 * @author Ludovic
 */
public class ItineraireTask extends AsyncTask<Void, Integer, Boolean> {
    /*******************************************************/
    /**
     * CONSTANTES.
     * /
     *******************************************************/
    private static final String TOAST_MSG = "Calcul de l'itinéraire en cours";
    private static final String TOAST_ERR_MAJ = "Impossible de trouver un itinéraire";

    /*******************************************************/
    private final ArrayList<LatLng> lstLatLng = new ArrayList<LatLng>();
    /**
     * ATTRIBUTS.
     * /
     *******************************************************/
    private Context context;
    private GoogleMap gMap;
    private double myLatitude;
    private double myLongitude;
    private String editDepart;
    private String editArrivee;
    private boolean animate;
    //private List<StartPointDistTime> startPoints;
    private TextView distanceRest;
    private TextView timeRest;
    private int distance;
    private int time;

    /*******************************************************/
    /** METHODES / FONCTIONS.
     /*******************************************************/
    /**
     * Constructeur.
     *
     * @param context
     * @param gMap
     * @param editDepart
     * @param editArrivee
     */
    public ItineraireTask(final Context context, final GoogleMap gMap, final String editDepart, final String editArrivee, boolean animate, TextView distanceRest, TextView timeRest) {
        this.context = context;
        this.gMap = gMap;
        this.editDepart = editDepart;
        this.editArrivee = editArrivee;
        if (!gMap.isMyLocationEnabled())
            cancel(true);
        this.myLatitude = gMap.getMyLocation().getLatitude();
        this.myLongitude = gMap.getMyLocation().getLongitude();
        this.animate = animate;
        //startPoints = new ArrayList<StartPointDistTime>();
        this.distanceRest = distanceRest;
        this.timeRest = timeRest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPreExecute() {
        if (animate)
            Toast.makeText(context, TOAST_MSG, Toast.LENGTH_LONG).show();
    }

    /***
     * {@inheritDoc}
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            //Construction de l'url à appeler
            final StringBuilder url = new StringBuilder("http://maps.googleapis.com/maps/api/directions/xml?sensor=false&language=fr&mode=bicycling");
            url.append("&origin=");
            if(animate)
            {
                Datacontainer.setDepart(this.myLatitude + "," + this.myLongitude);
                Datacontainer.setArrive(editArrivee);
            }
            if (editDepart.equals("Ma position"))
                url.append(this.myLatitude + "," + this.myLongitude);
            else
                url.append(editDepart.replace(' ', '+'));
            url.append("&destination=");
            url.append(editArrivee.replace(' ', '+'));

            /*if(!animate)
            {
                url.append("&waypoints=optimize:true|");
                url.append(this.myLatitude + "," + this.myLongitude);
            }*/

            //Appel du web service
            Log.i(this.getClass().getName(), "url direction : " + url.toString());
            final InputStream stream = new URL(url.toString()).openStream();

            //Traitement des données
            final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setIgnoringComments(true);

            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            final Document document = documentBuilder.parse(stream);
            document.getDocumentElement().normalize();

            //On récupère d'abord le status de la requête
            final String status = document.getElementsByTagName("status").item(0).getTextContent();
            if (!"OK".equals(status)) {
                return false;
            }

            //On récupère les steps
            final Element elementLeg = (Element) document.getElementsByTagName("leg").item(0);

            final NodeList distanceList = elementLeg.getElementsByTagName("distance");
            final Element distanceNode = (Element) distanceList.item(distanceList.getLength() - 1);
            distance = Integer.valueOf(distanceNode.getElementsByTagName("value").item(0).getTextContent());

            final NodeList timeList = elementLeg.getElementsByTagName("duration");
            final Element timeNode = (Element) timeList.item(timeList.getLength() - 1);
            time = Integer.valueOf(timeNode.getElementsByTagName("value").item(0).getTextContent());

            final NodeList nodeListStep = elementLeg.getElementsByTagName("step");
            final int length = nodeListStep.getLength();

            for (int i = 0; i < length; i++) {
                final Node nodeStep = nodeListStep.item(i);

                if (nodeStep.getNodeType() == Node.ELEMENT_NODE) {
                    final Element elementStep = (Element) nodeStep;


                   /* final Element elementStartPoint = (Element) elementStep.getElementsByTagName("start_location").item(0);
                    double lat = Double.valueOf(elementStartPoint.getElementsByTagName("lat").item(0).getTextContent());
                    double lng = Double.valueOf(elementStartPoint.getElementsByTagName("lng").item(0).getTextContent());

                    startPoints.add(new StartPointDistTime(new LatLng(lat,lng), distance, time));*/

                    //On décode les points du XML
                    decodePolylines(elementStep.getElementsByTagName("points").item(0).getTextContent());
                }
            }

        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            return true;
        }
    }

    /**
     * Méthode qui décode les points en latitudes et longitudes
     *
     * @param
     */
    private void decodePolylines(final String encodedPoints) {
        int index = 0;
        int lat = 0, lng = 0;

        while (index < encodedPoints.length()) {
            int b, shift = 0, result = 0;

            do {
                b = encodedPoints.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;

            do {
                b = encodedPoints.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            lstLatLng.add(new LatLng((double) lat / 1E5, (double) lng / 1E5));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostExecute(final Boolean result) {
        if (!result) {
            Toast.makeText(context, TOAST_ERR_MAJ, Toast.LENGTH_SHORT).show();
        } else {
            //On déclare le polyline, c'est-à-dire le trait (ici bleu) que l'on ajoute sur la carte pour tracer l'itinéraire
            final PolylineOptions polylines = new PolylineOptions();
            polylines.color(Color.BLUE);

            //On construit le polyline
            for (final LatLng latLng : lstLatLng) {
                polylines.add(latLng);
            }
            String reqElevation = "https://maps.googleapis.com/maps/api/elevation/json?path=36.578581,-118.291994|36.23998,-116.83171&samples=3";

            /*double distance = 0;
            double time = 0;
            boolean findMyPoint = false;
            for(StartPointDistTime point : this.startPoints)
            {
                if(findMyPoint)
                {
                    distance+=point.getDistance();
                    time+= point.getTime();
                }
                else if(point.getLatLng().latitude == this.myLatitude && point.getLatLng().longitude == this.myLongitude)
                {
                    findMyPoint = true;
                }
            }*/

            int km = (int) Math.floor(distance / 1000);
            int m = (int) Math.ceil(distance) % 1000;
            String distanceText = (km > 0 ? km + " Km " : "") + (m > 0 ? m + " m" : "");
            this.distanceRest.setText(distanceText);

            int hour = (int) Math.floor(time / 3600);
            int min = (int) Math.ceil(time % 3600)/60;
            String timeText = (hour > 0 ? hour + " h " : "") + (min > 0 ? min + " min" : "");
            this.timeRest.setText(timeText);

            //On déclare un marker vert que l'on placera sur le départ
            final MarkerOptions markerA = new MarkerOptions();
            markerA.position(lstLatLng.get(0));
            markerA.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            //On déclare un marker rouge que l'on mettra sur l'arrivée
            Datacontainer.setLastPoint(lstLatLng.get(lstLatLng.size() - 1));
            final MarkerOptions markerB = new MarkerOptions();
            markerB.position(lstLatLng.get(lstLatLng.size() - 1));
            markerB.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            //On met à jour la carte
            gMap.clear();
            if (this.animate) {
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(this.myLatitude, this.myLongitude), 18));
                gMap.addMarker(markerA);
            }
            gMap.addPolyline(polylines);
            gMap.addMarker(markerB);

            Datacontainer.setItineraireSetting(true);
        }
    }
}
