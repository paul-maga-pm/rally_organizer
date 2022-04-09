using Business.Services;
using Business.Services.Api;
using Business.Services.Implementation;
using Repository.Database;
using System.Configuration;

namespace Gui
{
    internal static class Program
    {

        static IRallyApplicationServices InitializeServices()
        {
            String databaseConnectionString = ConfigurationManager.ConnectionStrings["RallyEventDb"].ConnectionString;
            IDictionary<string, string> settings = new Dictionary<string, string>();

            settings.Add("ConnectionString", databaseConnectionString);

            var userRepo = new UserDatabaseRepository(settings);
            var participantRepo = new ParticipantDatabaseRepository(settings);
            var teamRepo = new TeamDatabaseRepository(settings);
            var rallyRepo = new RallyDatabaseRepository(settings);

            var userService = new UserService(userRepo);
            var participantService = new ParticipantService(participantRepo);
            var teamService = new TeamService(teamRepo);
            var rallyService = new RallyService(rallyRepo);

            var services = new RallyApplicationServicesImpl(userService, participantService, rallyService, teamService);

            return services;
        }

        [STAThread]
        static void Main()
        {

            ApplicationConfiguration.Initialize();
            
            var services = InitializeServices();
            Application.Run(new LoginForm(services));
        }
    }
}