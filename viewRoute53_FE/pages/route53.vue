<template>
  <div>
    <v-container>
      <v-card>
        <v-card-title> Route53 조회하려고? </v-card-title>
        <v-container>
          <v-card-actions>
            <!-- TODO: autocomplete multiple 설정 추가 -->
            <v-autocomplete
              v-model="values"
              :items="accountItems"
              outlined
              dense
              chips
              small-chips
              label="도메인을 선택하고 오른쪽 Reload 눌러."
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

    <v-container>
      <v-card>
        <v-card-title>
          <v-text-field
            v-model="search"
            append-icon="mdi-magnify"
            label="어떤 도메인 찾아?"
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
      </v-card>
    </v-container>

    <v-container>
      <v-card>
        {{ recordsItems }}
      </v-card>
    </v-container>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // autoCompletes
      values: ['what'],

      // Table
      search: '',
      headers: [
        { text: 'Record Name', value: 'recordName' },
        { text: 'Type', value: 'type' },
        { text: 'Value', value: 'ResourceRecords' },
        { text: 'TTL', value: 'recordExpire' },
        { text: 'Routing Policy', value: 'routingPolicy' },
      ],
      /*
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
      */
    }
  },

  computed: {
    recordsItems() {
      return this.$store.state.record.recordsItems
    },

    accountItems() {
      return this.$store.state.account.accountItems
      // return this.$store.state.record.test
    },
  },

  mounted() {
    this.$store.dispatch('account/loadAccountItems')
  },

  methods: {
    addHostZoneId() {
      // Account 등록하면, reload 해야함.
    },

    loadAccountItems() {
      this.$store.dispatch('account/loadAccountItems')
    },

    reloadHostZoneIds() {
      /*
      TODO: Account 정보를 받아서 /recordsets/{HostedZoneId} POST
            만약 다른 값이 있으면 저장, 모두 같으면 저장 안함.
            Account 정보를 받아서 /recordsets/{HostedZoneId} GET
            그 값을 Table에 출력
      */

      // FIXME: {HostedZoneId}로 변경해야함.
      this.$store.dispatch('record/loadRecordSetItems')
    },
  },
}
</script>

<style scoped></style>
