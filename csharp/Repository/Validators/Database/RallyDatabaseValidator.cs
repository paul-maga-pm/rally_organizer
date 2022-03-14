using Domain.Models;
using Repository.Validators.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Repository.Validators.Database.Postgresql
{
    public class RallyDatabaseValidator : IRallyValidator
    {
        public void Validate(Rally model)
        {
            if (model.EngineCapacity <= 0)
                throw new IRallyValidator.InvalidModelException("Engine capacity can't be negative!");
        }
    }
}
