using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Domain.Models
{
    public class RallyParticipant : Identifiable<long>
    {
        public String FirstName { get; set; }
        public String LastName { get; set; }

        public long RallyId { get; set; }
        public long TeamId { get; set; }

        public RallyParticipant(long rallyId, long teamId, String firstName, String lastName)
        {
            FirstName = firstName;
            LastName = lastName;
            RallyId = rallyId;
            TeamId = teamId;
        }

        public override bool Equals(object? obj)
        {
            return obj is RallyParticipant participant &&
                   base.Equals(obj) &&
                   Id == participant.Id &&
                   FirstName == participant.FirstName &&
                   LastName == participant.LastName &&
                   RallyId == participant.RallyId &&
                   TeamId == participant.TeamId;
        }

        public override int GetHashCode()
        {
            return HashCode.Combine(base.GetHashCode(), Id, FirstName, LastName, RallyId, TeamId);
        }
    }
}
