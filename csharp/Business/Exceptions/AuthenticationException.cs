
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;
using System.Threading.Tasks;

namespace Business.Exception
{
    public class AuthenticationException : Exceptions.ExceptionBaseClass
    {
        public AuthenticationException()
        {
        }

        public AuthenticationException(string? message) : base(message)
        {
        }

        public AuthenticationException(string? message, System.Exception? innerException) : base(message, innerException)
        {
        }

        protected AuthenticationException(SerializationInfo info, StreamingContext context) : base(info, context)
        {
        }
    }
}
