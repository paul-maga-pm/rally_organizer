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
        Participant? GetByName(String participantName);
        ICollection<Participant> GetMembersOfTeam(String teamName);
    }
}
