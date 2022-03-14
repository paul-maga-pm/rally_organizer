using Domain.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Repository.Interfaces
{
    public interface IParticipantRepository : IRepository<long, Participant>
    {
        Participant FindParticipantByName(String participantName);
        ICollection<Participant> FindMembersOfTeam(String teamName);
    }
}
