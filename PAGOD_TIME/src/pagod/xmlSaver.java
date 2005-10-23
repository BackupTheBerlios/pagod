package pagod;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.Serializer;
import nu.xom.ValidityException;


public class xmlSaver
{
	private Document	theDoc	= null;
	static File			theFile	= new File("pagod_time.xml");

	// fonction qui ouvre
	// ou ki crée le fichier si il n'existe pas
	public void load ()
	{

		if (theFile.exists())
		{
			Builder builder = new Builder();
			try
			{
				this.theDoc = builder.build(theFile);
			}
			catch (ValidityException e)
			{
				// TODO Bloc de traitement des exceptions généré automatiquement
				e.printStackTrace();
			}
			catch (ParsingException e)
			{
				// TODO Bloc de traitement des exceptions généré automatiquement
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Bloc de traitement des exceptions généré automatiquement
				e.printStackTrace();
			}
		}
		else
		{

			Element pagod = new Element("pagod");
			this.theDoc = new Document(pagod);
			Element allproject = new Element("allproject");
			allproject.appendChild("0:0:0");
			pagod.appendChild(allproject);
			this.write();

		}

		//		

	}

	public void addTime (String date, int time)
	{

		Element root = null;
		Element dateTemp = null;
		Element timeTemp = null;
		Element dayTemp = null;
		root = this.theDoc.getRootElement();
		// on parcours le document
		Elements daySet = root.getChildElements("day");
		int i = 0;
		boolean Existant = false;

		while (i < daySet.size() && !Existant)
		{
			dayTemp = daySet.get(i);
			dateTemp = dayTemp.getFirstChildElement("date");
			if ((dateTemp.getValue()).equals(date) )
			{
				
				Existant = true;

			}
			i++;

		}
		if (Existant)
		{
			
			timeTemp = dayTemp.getFirstChildElement("time");
			// recupere la valeur
			int val = timeFromString(timeTemp.getValue())+time;
			
			dayTemp.removeChildren();
			dateTemp = new Element("date");
			timeTemp = new Element("time");
			dateTemp.appendChild(date);
			timeTemp.appendChild(stringFromTime(val));
			dayTemp.appendChild(dateTemp);
			dayTemp.appendChild(timeTemp);
			
		}
		else
		{
			
			dateTemp = new Element("date");
			timeTemp = new Element("time");
			dayTemp = new Element("day");
			dateTemp.appendChild(date);
			timeTemp.appendChild(stringFromTime(time));
			dayTemp.appendChild(dateTemp);
			dayTemp.appendChild(timeTemp);
			root.appendChild(dayTemp);
		}
		this.write();

	}

	public String sumAndSetAllTime ()
	{
		int value = 0;
		Element root = this.theDoc.getRootElement();
		Element alltime = root.getFirstChildElement("allproject");
		Elements daySet = root.getChildElements("day");
		for (int i = 0; i < daySet.size(); i++)
		{
			Element day = daySet.get(i);
			Element time = day.getFirstChildElement("time");
			value += timeFromString(time.getValue());
			

		}
		alltime.removeChildren();
		alltime.appendChild(stringFromTime(value));
		return stringFromTime(value);

	}

	static int timeFromString (String timeInCar)
	{
		
		
		String tabTime []  = timeInCar.split(":");
		int h = (Integer.parseInt(tabTime[0]));
		int m = (Integer.parseInt(tabTime[1]));
		int s = (Integer.parseInt(tabTime[2]));
		return h*3600+m*60+s;

	}

	static String stringFromTime (int timeInSec)
	{
		int sec = timeInSec % 60;
		int reste = timeInSec / 60;
		
		int minute = reste % 60;
		int heure = reste / 60;
		
		return new String(heure + ":" + minute + ":" + sec);

	}

	public void write ()
	{
		FileOutputStream fos;
		try
		{
			this.sumAndSetAllTime();
			fos = new FileOutputStream(theFile.getName());
			Serializer output = new Serializer(fos, "ISO-8859-1");
			output.setIndent(2);
			output.write(this.theDoc);
			fos.close();
		}
		catch (FileNotFoundException e)
		{
			// TODO Bloc de traitement des exceptions généré automatiquement
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Bloc de traitement des exceptions généré automatiquement
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Bloc de traitement des exceptions généré automatiquement
			e.printStackTrace();
		}

	}
	public String getAllTime ()
	{
		Element root = this.theDoc.getRootElement();
		Element alltime = root.getFirstChildElement("allproject");
		return alltime.getValue();
	}
	public String getTodayTime ()
	{

		Element root = null;
		Element dateTemp = null;
		Element timeTemp = null;
		Element dayTemp = null;
		root = this.theDoc.getRootElement();
		// on parcours le document
		Elements daySet = root.getChildElements("day");
		int i = 0;
		boolean Existant = false;
		String motif = "dd-MM-yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(motif, Locale.FRANCE);
		while (i < daySet.size() && !Existant)
		{
			dayTemp = daySet.get(i);
			dateTemp = dayTemp.getFirstChildElement("date");
		

			if ((dateTemp.getValue()).equals(sdf.format(new Date())) )
			{
				
				Existant = true;

			}
			i++;

		}
		if (Existant)
		{
			dayTemp = daySet.get(i-1);
			timeTemp = dayTemp.getFirstChildElement("time");
			return timeTemp.getValue();
		}
		else
		{
			return "0:0:0";
		}
		
	}

	
}