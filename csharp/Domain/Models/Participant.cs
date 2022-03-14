using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Domain.Models
{
    public class Participant : Identifiable<long>
    {
        public String Name { get; set; }
       

        public Rally Rally { get; set; }
        public Team Team { get; set; }

        public Participant(Rally rally, Team team, String name)
        {
            Name = name;
            Rally = rally;
            Team = team;
        }

        public override bool Equals(object? obj)
        {
            return obj is Participant participant &&
                   base.Equals(obj) &&
                   Id == participant.Id &&
                   Name == participant.Name &&
                   Rally == participant.Rally &&
                   Team == participant.Team;
        }

        public override int GetHashCode()
        {
            return HashCode.Combine(base.GetHashCode(), Id, Name, Rally, Team);
        }
    }
}
