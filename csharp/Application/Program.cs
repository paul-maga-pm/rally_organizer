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

var t1 = teamRepo.Add(new Team("mamamama"));
var t2 = teamRepo.Add(new Team("jjjjjjjjjjj"));

var r1 = rallyRepo.Add(new Rally(10));
var r2 = rallyRepo.Add(new Rally(20));

participantRepo.Add(new Participant(r1, t1, "A"));
participantRepo.Add(new Participant(r1, t2, "B"));
participantRepo.Add(new Participant(r2, t1, "C"));
participantRepo.Add(new Participant(r2, t2, "D"));

teamRepo.FindTeamByName(t1.TeamName);
teamRepo.FindTeamByName(t2.TeamName);
rallyRepo.FindRallyByEngineCapacity(10);
rallyRepo.FindRallyByEngineCapacity(20);

participantRepo.FindMembersOfTeam(t1.TeamName);
participantRepo.FindMembersOfTeam(t2.TeamName);

var userRepo = new UserDatabaseRepository(properties);

//Console.WriteLine(userRepo.FindUserByUsername("Gigi").ToString());

//userRepo.Add(new User("", ""));
userRepo.Add(new User("Gigi", "Gigel"));


