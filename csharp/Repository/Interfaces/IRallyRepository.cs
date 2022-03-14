﻿using Domain.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Repository.Interfaces
{
    public interface IRallyRepository : IRepository<long, Rally>
    {
        Rally FindRallyByEngineCapacity(int engineCapacity);
    }
}
