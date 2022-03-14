using Domain.Models;
using Exceptions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;
using System.Threading.Tasks;

namespace Repository.Validators.Interfaces
{
    public interface IValidator<ID, M> where M : Identifiable<ID>
    {
        void Validate(M model);

        public class InvalidModelException : ExceptionBaseClass
        {
            public InvalidModelException()
            {
            }

            public InvalidModelException(string? message) : base(message)
            {
            }

            public InvalidModelException(string? message, Exception? innerException) : base(message, innerException)
            {
            }

            protected InvalidModelException(SerializationInfo info, StreamingContext context) : base(info, context)
            {
            }
        }
    }
}
