using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;


using Repository.Interfaces;
using Domain.Models;

namespace Business.Services
{
    public class TeamService
    {
        private ITeamRepository teamRepository;

        public TeamService(ITeamRepository teamRepository)
        {
            this.teamRepository = teamRepository;
        }

        public Team AddTeam(String teamName)
        {
            Team team = new Team(teamName);
            return teamRepository.Add(team);
        }

        public ICollection<Team> GetAllTeams()
        {
            return teamRepository.GetAll();
        }

        public Team? GetTeamByName(String teamName)
        {
            return teamRepository.GetByName(teamName); 
        }
    }
}
