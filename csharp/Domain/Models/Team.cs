using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Domain.Models
{
    public class Team : Identifiable<long>
    {
        public String TeamName { get; set; }

        public Team(string teamName)
        {
            TeamName = teamName;
        }

        public override bool Equals(object? obj)
        {
            return obj is Team team &&
                   base.Equals(obj) &&
                   Id == team.Id &&
                   TeamName == team.TeamName;
        }

        public override int GetHashCode()
        {
            return HashCode.Combine(base.GetHashCode(), Id, TeamName);
        }
    }
}
