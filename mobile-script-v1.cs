using System;
using System.IO;
using System.Net;

namespace test
{
	class MainClass
	{
		public static void Main (string[] args)
		{
			//location of the flask application server in AWS
			string test = string.Format ("http://34.217.162.221/");
			//initalize webrequest with the location of the server
			WebRequest requestObject = WebRequest.Create (test);
			//set the method of the request to get
			requestObject.Method = "GET";
			//set the response to null
			HttpWebResponse responseObject = null;
			//get the response of the request
			responseObject = (HttpWebResponse)requestObject.GetResponse ();

			//create a string to hold the result in
			string testResult = null;

			//use Stream/Stream Reader to evaluate the response
			using (Stream stream = responseObject.GetResponseStream())
			{
				StreamReader sr = new StreamReader(stream);
				testResult = sr.ReadToEnd();
				sr.Close();
				Console.Write (testResult);
			}
		}
	}
}