
-- Insert terms

INSERT INTO <schema>.term (term_short_name, term_year, term_type, time_stamp)
  VALUES ('1718v',2017,'SUMMER',current_timestamp);

INSERT INTO <schema>.term (term_short_name, term_year, term_type, time_stamp)
  VALUES ('1718i',2017,'WINTER',current_timestamp);

INSERT INTO <schema>.term (term_short_name, term_year, term_type, time_stamp)
  VALUES ('1617v',2016,'SUMMER',current_timestamp);

INSERT INTO <schema>.term (term_short_name, term_year, term_type, time_stamp)
  VALUES ('1617i',2016,'WINTER',current_timestamp);

INSERT INTO <schema>.term (term_short_name, term_year, term_type, time_stamp)
  VALUES ('1516v',2015,'SUMMER',current_timestamp);

INSERT INTO <schema>.term (term_short_name, term_year, term_type, time_stamp)
  VALUES ('1516i',2015,'WINTER',current_timestamp);

INSERT INTO <schema>.term (term_short_name, term_year, term_type, time_stamp)
  VALUES ('1415v',2014,'SUMMER',current_timestamp);

INSERT INTO <schema>.term (term_short_name, term_year, term_type, time_stamp)
  VALUES ('1415i',2014,'WINTER',current_timestamp);

INSERT INTO <schema>.term (term_short_name, term_year, term_type, time_stamp)
  VALUES ('1314v',2013,'SUMMER',current_timestamp);

INSERT INTO <schema>.term (term_short_name, term_year, term_type, time_stamp)
  VALUES ('1314i',2013,'WINTER',current_timestamp);

INSERT INTO <schema>.term (term_short_name, term_year, term_type, time_stamp)
  VALUES ('1213v',2012,'SUMMER',current_timestamp);

INSERT INTO <schema>.term (term_short_name, term_year, term_type, time_stamp)
  VALUES ('1213i',2012,'WINTER',current_timestamp);

INSERT INTO <schema>.term (term_short_name, term_year, term_type, time_stamp)
  VALUES ('1112v',2011,'SUMMER',current_timestamp);

INSERT INTO <schema>.term (term_short_name, term_year, term_type, time_stamp)
  VALUES ('1112i',2011,'WINTER',current_timestamp);

INSERT INTO <schema>.term (term_short_name, term_year, term_type, time_stamp)
  VALUES ('1011v',2010,'SUMMER',current_timestamp);

INSERT INTO <schema>.term (term_short_name, term_year, term_type, time_stamp)
  VALUES ('1011i',2010,'WINTER',current_timestamp);

-- Insert organization

INSERT INTO <schema>.organization(organization_full_name, organization_short_name, organization_address, organization_contact, organization_website, time_stamp)
  VALUES('<org.fullName>', '<org.shortName>', '<org.address>', '<org.contact>', '<org.website>', current_timestamp);

INSERT INTO <schema>.organization_version(organization_version, created_by, organization_full_name, organization_short_name, organization_address, organization_contact, organization_website, time_stamp)
  VALUES(1, 'Community', '<org.fullName>', '<org.shortName>', '<org.address>', '<org.contact>', '<org.website>', current_timestamp);

-- Insert users

<users:{user|
INSERT INTO <schema>.user_account (user_username, user_password, user_given_name, user_family_name, user_confirmed, user_email, user_locked)
  VALUES('<user.username>', '<user.password>', '<user.givenName>', '<user.familyName>', <user.confirmed>, '<user.email>', false);
}>

<reputations:{rep|
INSERT INTO <schema>.reputation (points, role, user_username)
  VALUES(<rep.points>, '<rep.role>', '<rep.username>');
}>