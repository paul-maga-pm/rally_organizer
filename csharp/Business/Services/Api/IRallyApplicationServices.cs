using Domain.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Business.Services.Api
{
    public interface IRallyApplicationServices
    {
        void LoginUser(String username, String password);
        
        Team AddTeam(string teamName);
        ICollection<Team> GetAllTeams();
        Team? GetTeamByName(string teamName);

        Rally AddRally(int engineCapacity);
        ICollection<Rally> GetAllRallies();

        Participant AddParticipant(Team participantTeam, Rally rallyParticipatesTo, String participantName);
        ICollection<Participant> GetAllMembersOfTeam(String teamName);
        Participant? GetParticipantByName(string teamName);
    }
}
