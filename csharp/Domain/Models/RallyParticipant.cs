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

        public Rally Rally { get; set; }
        public RallyTeam Team { get; set; }

        public RallyParticipant(Rally rally, RallyTeam team, String firstName, String lastName)
        {
            FirstName = firstName;
            LastName = lastName;
            Rally = rally;
            Team = team;
        }

        public override bool Equals(object? obj)
        {
            return obj is RallyParticipant participant &&
                   base.Equals(obj) &&
                   Id == participant.Id &&
                   FirstName == participant.FirstName &&
                   LastName == participant.LastName &&
                   Rally == participant.Rally &&
                   Team == participant.Team;
        }

        public override int GetHashCode()
        {
            return HashCode.Combine(base.GetHashCode(), Id, FirstName, LastName, Rally, Team);
        }
    }
}
