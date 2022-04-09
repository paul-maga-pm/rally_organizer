using Business.Services.Api;
using Domain.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Business.Services.Implementation
{
    public class RallyApplicationServicesImpl : IRallyApplicationServices
    {
        private UserService _userService;
        private ParticipantService _participantService;
        private RallyService _rallyService;
        private TeamService _teamService;

        public RallyApplicationServicesImpl(UserService userService, 
                                            ParticipantService participantService,
                                            RallyService rallyService,
                                            TeamService teamService)
        {
            _userService = userService;
            _participantService = participantService;
            _rallyService = rallyService;
            _teamService = teamService;
        }

        public Participant AddParticipant(Team participantTeam, Rally rallyParticipatesTo, string participantName)
        {
            return _participantService.AddParticipant(participantTeam, rallyParticipatesTo, participantName);
        }

        public Rally AddRally(int engineCapacity)
        {
            return _rallyService.AddRally(engineCapacity);
        }

        public Team AddTeam(string teamName)
        {
            return _teamService.AddTeam(teamName);
        }

        public ICollection<Participant> GetAllMembersOfTeam(string teamName)
        {
            return _participantService.GetAllMembersOfTeam(teamName);
        }

        public ICollection<Rally> GetAllRallies()
        {
            return _rallyService.GetAllRallies();
        }

        public ICollection<Team> GetAllTeams()
        {
            return _teamService.GetAllTeams();
        }

        public Participant? GetParticipantByName(string teamName)
        {
            return _participantService.GetParticipantByName(teamName);
        }

        public Team? GetTeamByName(string teamName)
        {
            return _teamService.GetTeamByName(teamName);
        }

        public void LoginUser(string username, string password)
        {
            _userService.LoginUser(username, password);
        }
    }
}
