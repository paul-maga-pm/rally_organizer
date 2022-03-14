// See https://aka.ms/new-console-template for more information

using Repository.Database;

IDictionary<string, string> properties = new Dictionary<string, string>();

properties.Add("ConnectionString", "Host=localhost;Username=postgres;Password=password;Database=rally_events");

var participantRepo = new ParticipantDatabaseRepository(properties);

foreach(var part in participantRepo.FindMembersOfTeam("Honda"))
{
    Console.WriteLine(part.Name);
}

