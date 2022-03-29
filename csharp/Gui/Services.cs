using Business.Services;
using Repository.Database;
using Repository.Interfaces;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;
using System.Threading.Tasks;


namespace Gui
{
    internal class Services
    {
        public static UserService UserService { get; set; }
        public static ParticipantService ParticipantService { get; set; }
        public static RallyService RallyService { get; set; }
        public static TeamService TeamService { get; set; }

        private static IDictionary<string, string> properties = new Dictionary<string, string>();
        public static void InitializeServices()
        {
            InitializeRepositoryProps();

            UserService = new UserService(CreateUserRepository());
            ParticipantService = new ParticipantService(CreateParticipantRepository());
            RallyService = new RallyService(CreateRallyRepository());
            TeamService = new TeamService(CreateTeamRepository());
        }

        private static ITeamRepository CreateTeamRepository()
        {
            return new TeamDatabaseRepository(properties);
        }

        private static IRallyRepository CreateRallyRepository()
        {
            return new RallyDatabaseRepository(properties);
        }

        private static IParticipantRepository CreateParticipantRepository()
        {
            return new ParticipantDatabaseRepository(properties);
        }

        private static void InitializeRepositoryProps()
        {
            String connectionString = ConfigurationManager.ConnectionStrings["RallyEventDb"].ConnectionString;

            properties.Add("ConnectionString", connectionString);
        }

        static IUserRepository CreateUserRepository()
        {
            return new UserDatabaseRepository(properties);
        }

    }
}
