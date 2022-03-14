// See https://aka.ms/new-console-template for more information

using Domain.Models;
using log4net.Config;
using Repository.Database;
using System.Configuration;

XmlConfigurator.Configure();

IDictionary<string, string> properties = new Dictionary<string, string>();

String connectionString = ConfigurationManager.ConnectionStrings["RallyEventDb"].ConnectionString;

properties.Add("ConnectionString", "Host=localhost;Username=postgres;Password=password;Database=rally_events");
var participantRepo = new ParticipantDatabaseRepository(properties);
var rallyRepo = new RallyDatabaseRepository(properties);
var teamRepo = new TeamDatabaseRepository(properties);


var rally = new Rally(120);
var team = new Team("Handralai");

rally = rallyRepo.Add(rally);
team = teamRepo.Add(team);

var participant = participantRepo.Add(new Participant(rally, team, "Part"));
Console.WriteLine(participant);

foreach(var part in participantRepo.FindMembersOfTeam("Honda"))
{
    Console.WriteLine(part.Name);
}


foreach(var _rally in rallyRepo.FindAll())
{
    Console.WriteLine(_rally.EngineCapacity + " " + _rally.NumberOfParticipants);
}


Console.WriteLine(teamRepo.FindTeamByName("Handralai").TeamName);

foreach(var member in participantRepo.FindMembersOfTeam("Handralai"))
{
    Console.WriteLine(member.Name);
}

