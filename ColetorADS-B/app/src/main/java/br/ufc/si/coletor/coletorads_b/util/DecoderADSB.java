package br.ufc.si.coletor.coletorads_b.util;
/**
 *  This file is part of org.opensky.libadsb.
 *
 *  org.opensky.libadsb is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  org.opensky.libadsb is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with org.opensky.libadsb.  If not, see <http://www.gnu.org/licenses/>.
 */

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opensky.libadsb.Decoder;
import org.opensky.libadsb.Position;
import org.opensky.libadsb.PositionDecoder;
import org.opensky.libadsb.tools;
import org.opensky.libadsb.exceptions.BadFormatException;
import org.opensky.libadsb.msgs.AirbornePositionMsg;
import org.opensky.libadsb.msgs.AirspeedHeadingMsg;
import org.opensky.libadsb.msgs.EmergencyOrPriorityStatusMsg;
import org.opensky.libadsb.msgs.IdentificationMsg;
import org.opensky.libadsb.msgs.ModeSReply;
import org.opensky.libadsb.msgs.OperationalStatusMsg;
import org.opensky.libadsb.msgs.SurfacePositionMsg;
import org.opensky.libadsb.msgs.TCASResolutionAdvisoryMsg;
import org.opensky.libadsb.msgs.VelocityOverGroundMsg;

/**
 * ADS-B decoder example: It reads STDIN line-by-line. It should be fed with
 * comma-separated timestamp and message.
 * @author Matthias Schäfer <schaefer@sero-systems.de>
 */
public class DecoderADSB {
	// tmp variables for the different message types
	private ModeSReply msg;
	private IdentificationMsg ident;
	private EmergencyOrPriorityStatusMsg status;
	private AirspeedHeadingMsg airspeed;
	private AirbornePositionMsg airpos;
	private OperationalStatusMsg opstat;
	private SurfacePositionMsg surfpos;
	private TCASResolutionAdvisoryMsg tcas;
	private VelocityOverGroundMsg veloc;
	private String icao24;

	// we store the position decoder for each aircraft
	HashMap<String, PositionDecoder> decs;
	private PositionDecoder dec;

	public DecoderADSB() {
		decs = new HashMap<String, PositionDecoder>();
	}

	public String getIcao24(long timestamp, String raw)  {
		try {
			msg = Decoder.genericDecoder(raw);
		} catch (BadFormatException e) {
			return "Malformed message! ";
		} catch (Exception e) {
			return "Malformed message! ";
		}


		if (tools.isZero(msg.getParity()) || msg.checkParity()) { // CRC is ok
			icao24 = tools.toHexString(msg.getIcao24());

			List<String> to_remove = new ArrayList<String>();
			for (String key : decs.keySet()) {
				if (decs.get(key).getLastUsedTime() < timestamp - 3600) {
					to_remove.add(key);
				}
			}

			for (String key : to_remove)
				decs.remove(key);

		}
		return icao24;
	}

	public String getTypeMessage(String raw){
		try {
			msg = Decoder.genericDecoder(raw);
		} catch (BadFormatException e) {
			return "Malformed message! Skipping it";
		} catch (Exception e) {
			return "Malformed message! Skipping it";
		}

		switch (msg.getType()) {
			case ADSB_AIRBORN_POSITION: return "AIRBORN_POSITION";
			case ADSB_SURFACE_POSITION: return "SURFACE_POSITION";
			case ADSB_EMERGENCY: return "EMERGENCY";
			case ADSB_AIRSPEED: return "AIRSPEED";
			case ADSB_IDENTIFICATION: return "IDENTIFICATION";
			case ADSB_STATUS: return "STATUS";
			case ADSB_TCAS: return "TCAS";
			case ADSB_VELOCITY: return "VELOCITY";
			default:return "UNKNOW";
		}

	}

	public Map<String, String> decodeMsg(long timestamp, String raw) throws Exception {
		Map<String, String> infos = new HashMap<String, String>();

		try {
			msg = Decoder.genericDecoder(raw);
		} catch (BadFormatException e) {
			infos.put("Message", "Malformed message! Skipping it...");
			return infos;
		}


		if (tools.isZero(msg.getParity()) || msg.checkParity()) {
			icao24 = tools.toHexString(msg.getIcao24());
			

			List<String> to_remove = new ArrayList<String>();
			for (String key : decs.keySet())
				if (decs.get(key).getLastUsedTime() < timestamp-3600)
					to_remove.add(key);
			
			for (String key : to_remove)
				decs.remove(key);


			switch (msg.getType()) {
			case ADSB_AIRBORN_POSITION:
				airpos = (AirbornePositionMsg) msg;

				// decode the position if possible
				if (decs.containsKey(icao24)) {
					dec = decs.get(icao24);
					airpos.setNICSupplementA(dec.getNICSupplementA());
					Position current = dec.decodePosition(timestamp, airpos);
					if (current == null) {
						infos.put("Message", "Cannot decode position yet.");
					}else {
						infos.put("Message", "Now at position (" + current.getLatitude() + "," + current.getLongitude() + ")");
					}
				}
				else {
					dec = new PositionDecoder();
					dec.decodePosition(timestamp, airpos);
					decs.put(icao24, dec);
					infos.put("Message", "First position.");
				}
				infos.put("Message2", "Horizontal containment radius is"+airpos.getHorizontalContainmentRadiusLimit()+"m");
				infos.put("Message3", "Altitude is " + (airpos.hasAltitude() ? airpos.getAltitude() : "unknown") +"m");
				return infos;
			case ADSB_SURFACE_POSITION:
				surfpos = (SurfacePositionMsg) msg;

				System.out.print("[" + icao24 + "]: ");

				// decode the position if possible
				if (decs.containsKey(icao24)) {
					dec = decs.get(icao24);
					Position current = dec.decodePosition(timestamp, surfpos);
					if (current == null)
						infos.put("Message", "Cannot decode position yet.");
					else
						infos.put("Message", "Now at position ("+current.getLatitude()+","+current.getLongitude()+")");
				}
				else {
					dec = new PositionDecoder();
					dec.decodePosition(timestamp, surfpos);
					decs.put(icao24, dec);
					infos.put("Message", "First position.");
				}
				infos.put("Message2", "Horizontal containment radius is " + surfpos.getHorizontalContainmentRadiusLimit() + " m");
				if (surfpos.hasValidHeading())
					infos.put("Message3", "Heading is "+surfpos.getHeading()+" m");

				infos.put("Message4", "Airplane is on the ground.");
				return infos;
			case ADSB_EMERGENCY:
				status = (EmergencyOrPriorityStatusMsg) msg;
				infos.put("Message1", "[" + icao24 + "]: " + status.getEmergencyStateText());
				infos.put("Message2", "Mode A code is " + status.getModeACode()[0] + status.getModeACode()[1] + status.getModeACode()[2] + status.getModeACode()[3]);
				return infos;
			case ADSB_AIRSPEED:
				airspeed = (AirspeedHeadingMsg) msg;
				infos.put("Message1", "[" + icao24 + "]: Airspeed is " + (airspeed.hasAirspeedInfo() ? airspeed.getAirspeed() + " m/s" : "unkown"));
				if (airspeed.hasHeadingInfo())
					infos.put("Message2", "Heading is "+(airspeed.hasHeadingInfo() ? airspeed.getHeading()+ "°" : "unkown"));
				if (airspeed.hasVerticalRateInfo())
					infos.put("Message3", "Vertical rate is "+ (airspeed.hasVerticalRateInfo() ? airspeed.getVerticalRate()+" m/s" : "unkown"));
				return infos;
			case ADSB_IDENTIFICATION:
				ident = (IdentificationMsg) msg;
				infos.put("Message1", "[" + icao24 + "]: Callsign is " + new String(ident.getIdentity()));
				infos.put("Message2", "Category: " + ident.getCategoryDescription());
				return infos;
			case ADSB_STATUS:
				opstat = (OperationalStatusMsg) msg;
				PositionDecoder dec;
				if (decs.containsKey(icao24))
					dec = decs.get(icao24);
				else {
					dec = new PositionDecoder();
					decs.put(icao24, dec);
				}
				dec.setNICSupplementA(opstat.getNICSupplementA());
				if (opstat.getSubtypeCode() == 1)
					dec.setNICSupplementC(opstat.getNICSupplementC());
				infos.put("Message1", "["+icao24+"]: Using ADS-B version "+opstat.getVersion());
				infos.put("Message2", "Has ADS-B IN function: " + opstat.has1090ESIn());
				return infos;
			case ADSB_TCAS:
				tcas = (TCASResolutionAdvisoryMsg) msg;
				infos.put("Message1", "["+icao24+"]: TCAS Resolution Advisory completed: " + tcas.hasRATerminated());
				infos.put("Message2", "Threat type is " + tcas.getThreatType());
				if (tcas.getThreatType() == 1) // it's a icao24 address
					infos.put("Message3", "Threat identity is 0x"+String.format("%06x", tcas.getThreatIdentity()));
				return infos;
			case ADSB_VELOCITY:
				veloc = (VelocityOverGroundMsg) msg;
				infos.put("Message1", "[" + icao24 + "]: Velocity is " + (veloc.hasVelocityInfo() ? veloc.getVelocity() : "unknown") + " m/s");
				infos.put("Message2", "Heading is " + (veloc.hasVelocityInfo() ? veloc.getHeading() : "unknown") + " degrees");
				infos.put("Message3", "Vertical rate is " + (veloc.hasVerticalRateInfo() ? veloc.getVerticalRate() : "unknown") + " m/s");
				return infos;
			default:
				infos.put("Message", "[" + icao24 + "]: Unknown message with downlink format " + msg.getDownlinkFormat());
				return infos;
			}
		}
		else { // CRC failed
			infos.put("Message", "Message seems to contain biterrors.");
			return infos;
		}
	}

}
