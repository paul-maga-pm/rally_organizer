using DatabaseConnectionUtils;
using Domain.Models;
using Repository.Interfaces;
using Repository.Validators.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Repository.Validators.Database.Postgresql
{
    public class TeamDatabaseValidator : ITeamValidator
    {
        public void Validate(Team model)
        {
            throw new NotImplementedException();
        }
    }
}
