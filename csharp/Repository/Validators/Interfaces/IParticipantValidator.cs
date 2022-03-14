﻿using Domain.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Repository.Validators.Interfaces
{
    public interface IParticipantValidator: IValidator<long, Participant>
    {
    }
}
