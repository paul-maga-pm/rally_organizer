using Domain.Models;
using Repository.Utils;
using Repository.Validators.Interfaces;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Repository.Validators.Database.Postgresql
{
    public class ParticipantDatabaseValidator : IParticipantValidator
    {
        private DatabaseUtils databaseUtils;
        private static readonly String findTeamByIdSqlString = "select * from teams where team_id=@team_id_param";
        private static readonly String findRallyByIdSqlString = "select * from rallies where rally_id=@rally_id_param";

        public ParticipantDatabaseValidator(IDictionary<String, String> connectionProperties)
        {
            this.databaseUtils = new DatabaseUtils(connectionProperties);
        }

        public void Validate(Participant model)
        {
            String errors = "";

            if (!TeamOfParticipantExists(model))
                errors += "Selected team doesn't exist!\n";

            if (!RallyThatParticipantIsAssignedToExists(model))
                errors += "Selected rally doesn't exist!\n";

            if (model.Name.Equals(""))
                errors += "Participant's name can't be empty!";

            if (!errors.Equals(""))
                throw new IRallyValidator.InvalidModelException(errors);
        }

        private Boolean TeamOfParticipantExists(Participant participant)
        {
            using(var connection = databaseUtils.GetConnection())
            {
                IDbCommand command = connection.CreateCommand();
                command.CommandText = findTeamByIdSqlString;
                
                IDbDataParameter teamIdParam = command.CreateParameter();
                teamIdParam.ParameterName = "@team_id_param";
                teamIdParam.Value = participant.Team.Id;

                command.Parameters.Add(teamIdParam);

                using(IDataReader reader = command.ExecuteReader())
                {
                    return reader.Read();
                }
            }
        }

        private Boolean RallyThatParticipantIsAssignedToExists(Participant participant)
        {
            using (var connection = databaseUtils.GetConnection())
            {
                IDbCommand command = connection.CreateCommand();
                command.CommandText = findRallyByIdSqlString;

                IDbDataParameter rallyIdParam = command.CreateParameter();
                rallyIdParam.ParameterName = "@rally_id_param";
                rallyIdParam.Value = participant.Rally.Id;

                command.Parameters.Add(rallyIdParam);

                using (IDataReader reader = command.ExecuteReader())
                {
                    return reader.Read();
                }
            }
        }

    }
}
