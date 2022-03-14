using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Domain.Models
{
    public class Rally : Identifiable<long>  
    {
        public int EngineCapacity { get; set; }
        public int NumberOfParticipants { get; set; }   

        public Rally(int engineCapacity)
        {
            EngineCapacity = engineCapacity;
            NumberOfParticipants = 0;
        }

        public Rally(int engineCapacity, int numberOfParticipants)
        {
            NumberOfParticipants = numberOfParticipants;
            EngineCapacity = engineCapacity;
        }

        public override bool Equals(object? obj)
        {
            return obj is Rally rally &&
                   base.Equals(obj) &&
                   Id == rally.Id &&
                   EngineCapacity == rally.EngineCapacity &&
                   NumberOfParticipants == rally.NumberOfParticipants;
        }

        public override int GetHashCode()
        {
            return HashCode.Combine(base.GetHashCode(), Id, EngineCapacity, NumberOfParticipants);
        }
    }
}
