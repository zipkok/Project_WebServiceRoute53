<template>
  <div>
    <v-container>
      <v-card>
        <v-card-title> Route53 할 사람?</v-card-title>
        <v-container>
          <v-card-actions>
            <v-autocomplete
              v-model="values"
              :items="items"
              outlined
              dense
              chips
              small-chips
              label="도메인을 선택하세요."
              multiple
              class="pr-5 pt-7"
            />

            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <v-btn
                  color="primary"
                  v-bind="attrs"
                  v-on="on"
                  @click="addHostZoneId"
                >
                  <v-icon> mdi-account-plus</v-icon>
                </v-btn>
              </template>
              <span>DNS Zone 등록</span>
            </v-tooltip>

            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <v-btn
                  color="red"
                  v-bind="attrs"
                  v-on="on"
                  @click="reloadHostZoneIds"
                >
                  <v-icon>mdi-reload</v-icon>
                </v-btn>
              </template>
              <span>DNS Zone 새로고침</span>
            </v-tooltip>
          </v-card-actions>
        </v-container>
      </v-card>
    </v-container>
    <v-card>
      {{ recordsItems }}
    </v-card>

    <v-container>
      <v-card-title>
        <v-text-field
          v-model="search"
          append-icon="mdi-magnify"
          label="Search"
          single-line
          hide-details
          outlined
        ></v-text-field>
      </v-card-title>
      <v-data-table
        :headers="headers"
        :items="recordsItems"
        :search="search"
      ></v-data-table>
    </v-container>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // autoCompletes
      items: [
        'wooboem.com - ap-northeast-2 - 우리팀',
        'wooboem.com - us-east-1 - 우리팀',
        'kyoung.com - us-east-1 - 다른팀',
        'kyoung.com - ap-northeast-2 - 다른팀',
      ],
      values: [],

      // Table
      search: '',
      headers: [
        { text: 'Record Name', value: 'recordName' },
        { text: 'Type', value: 'type' },
        { text: 'Value', value: 'ResourceRecords' },
        { text: 'TTL', value: 'recordExpire' },
        { text: 'Routing Policy', value: 'routingPolicy' },
      ],
      desserts: [
        {
          recordName: 'woobeom',
          domainName: 'test.com',
          dnsType: 'CNAME',
          recordSet: 'woobeom.test.com',
          recordExpire: 600,
          routingPolicy: 'Simple',
        },
        {
          recordName: 'kyoungMin',
          domainName: 'test.com',
          dnsType: 'A',
          recordSet: '1.1.1.1',
          recordExpire: 3600,
          routingPolicy: 'Simple',
        },
      ],
    }
  },

  computed: {
    recordsItems() {
      return this.$store.state.record.recordsItems
    },
  },

  methods: {
    addHostZoneId() {
      //
    },

    reloadHostZoneIds() {
      this.$store.dispatch('record/loadRecordSetItems')
    },
  },
}
</script>

<style scoped></style>
